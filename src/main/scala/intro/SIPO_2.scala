// See README.md for license details.

package intro

import chisel3._
import chisel3.util._

class SIPO_2(n: Int) extends Module {
  val io = IO(new Bundle{
    val si = Flipped(Decoupled(Bool()))
    val po = Decoupled(UInt(n.W))
  })
  // regs
  val dat = RegInit(VecInit(Seq.fill(4)(false.B)))
  val busy = RegInit(false.B)
  
  // don't accept while busy
  io.si.ready := !busy
  
  val cnt = SIPO_2.counter((n - 1).U, io.si.valid & !busy)
  
  when (io.si.valid & !busy) {
    dat(cnt) := io.si.bits
  }
  when((cnt === (n - 1).U) & io.si.valid) {
    busy := true.B
  } .elsewhen(busy && io.po.ready) {
    busy := false.B
  }
  io.po.valid := busy
  io.po.bits  := dat.asUInt 
}

object SIPO_2 extends App {
  chisel3.Driver.execute(args, () => new SIPO_2(4))

  def counter(max: UInt, ena: Bool) = {
    val x = RegInit(0.asUInt(max.getWidth.W))
    when(ena){
      x := Mux(x === max, 0.U, x + 1.U)
    }
    x
  }
}
