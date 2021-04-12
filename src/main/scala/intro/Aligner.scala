// See README.md for license details.

package intro

import chisel3._

class Aligner(val data_width: Int, val num: Int) extends Module {
  val io = IO(new Bundle{
    val incomming = Vec(num, new AlignerIF(data_width)) 
    val outgoing  = Vec(num, Flipped(new AlignerIF(data_width))) 
  })
  var i = 0.U(num.W)
  io.outgoing.map(_.en).foreach(
    e => e := false.B 
  )
  io.outgoing.map(_.data).foreach(
    d => d := 0.U(data_width.W)
  )

  for(v <- io.incomming) {
    when (v.en) {
      io.outgoing(i).en := v.en
      io.outgoing(i).data := v.data
      //i = i + 1.U //
      i = i + v.en //
    }
  }
}

object Aligner extends App {
  chisel3.Driver.execute(args, () => new Aligner(8, 4))
}

