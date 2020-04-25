// See README.md for license details.

package intro

import chisel3._

class MyMod extends Module {
  val io = IO(new Bundle{
    val a = Input  (UInt(8.W))
    val b = Input  (UInt(8.W))
    val max = Output (UInt(8.W))
  })
  val c = Module(new Mux2)
  c.io.sel := io.a < io.b;
  c.io.in0 := io.a
  c.io.in1 := io.b
  io.max   := c.io.out
}

object MyMod extends App {
  chisel3.Driver.execute(args, () => new MyMod)
}


