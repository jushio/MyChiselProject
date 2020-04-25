  // See README.md for license details.

package intro

import scala.math.ceil

import chisel3._
import chisel3.util._

// clock rate : 20 MHz
// parity     : not supported
// rx order   : data is sent from LSB
// input      : 
//  data_rate: 
//             1:   9600bps 
//             2:  19200bps
//             4:  38400bps
//             6:  57600bps
//            12: 115200bps 
class UART_RX(data_rate_type: Int) extends Module {
  val DATA_WIDTH  = 8
  val CLK_RATE    = 20000000
  val count = ceil(1.0f * CLK_RATE / (9600 * data_rate_type)) toInt
  
  val io = IO(new Bundle{
    val RX = Input(Bool())
    val DO = Decoupled(UInt(DATA_WIDTH.W))
    val BUSY = Output(Bool())
  })


  // wire 
  val detect          = WireInit(false.B)
  val sampling_timing = WireInit(false.B) 
  val rx_end          = WireInit(false.B) 
  // regs
  val async               = RegInit(0.U(3.W)) 
  val data                = RegInit(0.U(DATA_WIDTH.W))
  val busy                = RegInit(false.B)
  val data_valid          = RegInit(false.B)
  val sampling_timing_cnt = UART_RX.counter((count - 1).U, busy, !busy)
  val data_cnt            = UART_RX.counter((DATA_WIDTH - 1).U, sampling_timing, !busy)

  //---------------
  // assign 
  //---------------
  // port
  io.BUSY         := busy
  io.DO.valid     := data_valid 
  io.DO.bits      := data
  // internal
  detect          := !busy & !async(2) & async(1) 
  sampling_timing := busy & (sampling_timing_cnt === (count - 1).U)
  rx_end          := busy & (data_cnt === (DATA_WIDTH - 1).U) & sampling_timing

  //---------------
  // State 
  //---------------
  // rx sampling
  async := Cat(async << 1, io.RX)

  // busy
  when (rx_end) {
    busy := false.B
  }.elsewhen(!busy & detect) {
    busy := true.B
  }

  // data_valid
  when (data_valid & io.DO.ready) {
    data_valid := false.B
  }.elsewhen (rx_end) {
    data_valid := true.B
  }

  // data
  //  Data is sent from LSB
  when (sampling_timing) {
    data := Cat(async(2), (data >> 1)(DATA_WIDTH - 2, 1))
  }

}

object UART_RX extends App {
  chisel3.Driver.execute(args, () => new UART_RX(12))

  def counter(max: UInt, ena: Bool, clear: Bool) = {
    val x = RegInit(0.asUInt(max.getWidth.W))
    when(clear) {
      x := 0.U
    }.elsewhen(ena){
      x := Mux(x === max, 0.U, x + 1.U)
    }
    x
  }
}
