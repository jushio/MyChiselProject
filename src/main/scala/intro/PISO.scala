// See README.md for license details.

package intro

import chisel3._
import chisel3.util._

class PISO(n: Int) extends Module {
  val io = IO(new Bundle{
    val pi = Flipped(Decoupled(UInt(n.W)))
    val so = Decoupled(Bool())
  })
  // regs
  val dat = RegInit(0.U(n.W)) 
  val busy = RegInit(false.B)
  val cnt = SIPO.counter((n - 1).U, io.so.valid & io.so.ready) 

  // don't accept while data remains
  io.pi.ready := !busy
 
  io.so.valid := busy
  io.so.bits  := dat(cnt) 

  // State 
  when (io.pi.valid & !busy) {
    dat := io.pi.bits
  }
  when(io.pi.valid & !busy) {
    busy := true.B
  } .elsewhen((cnt === (n - 1).U) & io.so.valid & io.so.ready) {
    busy := false.B
  }
}

object PISO extends App {
  chisel3.Driver.execute(args, () => new PISO(4))

  def counter(max: UInt, ena: Bool) = {
    val x = RegInit(0.asUInt(max.getWidth.W))
    when(ena){
      x := Mux(x === max, 0.U, x + 1.U)
    }
    x
  }
}
