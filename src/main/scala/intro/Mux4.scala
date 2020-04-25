// See README.md for license details.

package intro

import chisel3._


class Mux4 extends Module {
  val io = IO(new Bundle{
    val sel = Input  (UInt(2.W)) 
    val in0 = Input  (UInt(8.W))
    val in1 = Input  (UInt(8.W))
    val in2 = Input  (UInt(8.W))
    val in3 = Input  (UInt(8.W))
    val out = Output (UInt(8.W))
  })
  io.out := Mux(io.sel(1), 
      Mux(io.sel(0), io.in3, io.in2), 
      Mux(io.sel(0), io.in1, io.in0)) 
}

object Mux4 extends App {
  chisel3.Driver.execute(args, () => new Mux4)
}


