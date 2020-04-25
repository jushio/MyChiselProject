// See README.md for license details.

package intro

import chisel3._


class Mux2 extends Module {
  val io = IO(new Bundle{
    val sel = Input    (Bool())
    val in0 = Input  (UInt(8.W))
    val in1 = Input  (UInt(8.W))
    val out = Output (UInt(8.W))
  })
  io.out := Mux(io.sel, io.in1, io.in0) 
}

object Mux2 extends App {
  chisel3.Driver.execute(args, () => new Mux2)
}


