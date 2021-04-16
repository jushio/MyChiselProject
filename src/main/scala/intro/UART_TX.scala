  // See README.md for license details.

package intro

import scala.math.ceil

import chisel3._
import chisel3.util._

// clock rate : 100 MHz
// parity     : not supported
// tx order   : data is sent from LSB
// input      : 
//  data_rate: 
//             1:   9600bps 
//             2:  19200bps
//             4:  38400bps
//             6:  57600bps
//            12: 115200bps 
class UART_TX(data_rate_type: Int, CLK_RATE: Int = 100000000) extends Module {
  val DATA_WIDTH  = 8
  val count = ceil((1.0f * CLK_RATE) / (9600 * data_rate_type)) toInt
  
  val io = IO(new Bundle{
    val TX    = Output(Bool())
    val DI    = Flipped(Decoupled(UInt(DATA_WIDTH.W)))
    val BUSY  = Output(Bool())
  })


  // wire 
  val update_timing = WireInit(false.B) 
  val tx_end        = WireInit(false.B) 
  // regs
  val data              = RegInit(~0.U((DATA_WIDTH+2).W))  // {start, send_data}
  val busy              = RegInit(false.B)
  val update_timing_cnt = UART_TX.counter((count - 1).U, busy, !busy)
  val data_cnt          = UART_TX.counter((DATA_WIDTH+1).U, update_timing, !busy)
  val data_rd_idx       = (DATA_WIDTH+1).U - data_cnt ;
  //---------------
  // assign 
  //---------------
  // port
  io.BUSY         := busy
  io.DI.ready     := !busy
  io.TX           := true.B
  when (busy) { 
    // Send From MSB
    io.TX         := data(data_rd_idx)
  }.otherwise {
    io.TX         := true.B
  } 
  // internal
  update_timing   := busy & (update_timing_cnt === (count - 1).U)
  tx_end          := busy & (data_cnt === (DATA_WIDTH+1).U) & update_timing

  //---------------
  // State 
  //---------------
  // busy
  when (tx_end) {
    busy := false.B
  }.elsewhen(!busy & io.DI.valid) {
    busy := true.B
  }

  // data
  when (!busy & io.DI.valid) {
    data := Cat(false.B, io.DI.bits, true.B)
  }
}

object UART_TX extends App {
  chisel3.Driver.execute(args, () => new UART_TX(12))

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
