// See README.md for license details.

package intro

import chisel3._


class MuxX(val n: Int) extends Module {
  require(util.isPow2(n))
  val log2n = util.log2Ceil(n)
  val io = IO(new Bundle{
    val sel = Input  (UInt(log2n.W)) 
    val ins = Input  (Vec(n, SInt(8.W)))
    val out = Output (SInt(8.W))
  })
  io.out := io.ins(io.sel) 
}

object MuxX extends App {
  chisel3.Driver.execute(args, () => new MuxX(4))
}


