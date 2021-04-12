// See README.md for license details.

package intro

import chisel3.iotesters.PeekPokeTester


class AlignerTests(c: Aligner) extends PeekPokeTester(c) {
  for (i <- 0 until 4) {
    poke(c.io.incomming(i).en, i % 2)
    poke(c.io.incomming(i).data, 10-i)
  }
  for (i <- 0 until 4) {
    expect(c.io.outgoing(i).en, if(i < 2) 1 else 0)
    if(i<2)
      expect(c.io.outgoing(i).data, 10-(i*2+1))
  }
}


