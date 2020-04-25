// See README.md for license details.

package intro

import chisel3._

class ChiselShift extends Module {
  val io = IO(new Bundle{
    val a = Input  (UInt(8.W))
    val signed = Input  (SInt(8.W))
    val rs = Output (UInt(8.W)) 
    val ls = Output (UInt(8.W))
    val signed_rs = Output (SInt(8.W)) 
    val signed_ls = Output (SInt(8.W))
    val signed2unsigned = Output (UInt(8.W))
    val signedrs2unsigned = Output (UInt(8.W))
  })
  io.rs := io.a >> 1
  io.ls := io.a << 1
  io.signed_rs := io.signed >> 1
  io.signed_ls := io.signed << 1
  io.signed2unsigned := io.signed.asUInt
  io.signedrs2unsigned := (io.signed >> 1).asUInt
}

object ChiselShift extends App {
  chisel3.Driver.execute(args, () => new ChiselShift)
}


