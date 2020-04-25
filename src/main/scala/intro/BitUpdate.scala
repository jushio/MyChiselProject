// See README.md for license details.

package intro

import chisel3._
import chisel3.util._

class BitUpdate(n: Int) extends Module {
  val io = IO(new Bundle{
    val si = Flipped(Decoupled(Bool()))
    val po = Decoupled(UInt(n.W))
  })
  val dat = RegInit(0.U(4.W)) 
  val dat2 = Reg(Vec(4, Bool())) 
  io.si.ready := true.B
  // dat(0) := io.si.bits // Compile Error
  // dat := Cat(dat(3), io.si.bits, dat(1,0)) // Good
  dat := Cat(dat(3), io.si.bits, dat(1,0))
  dat2(1) := io.si.bits 

  io.po.valid := true.B
  io.po.bits  := Cat(dat, dat2.asUInt)
}

object BitUpdate extends App {
  chisel3.Driver.execute(args, () => new BitUpdate(8))
}
