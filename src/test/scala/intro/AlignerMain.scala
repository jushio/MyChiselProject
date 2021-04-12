// See README.md for license details.

package intro

import chisel3._


object AlignerMain extends App {
  iotesters.Driver.execute(args, () => new Aligner(8, 4)) {
    c => new AlignerTests(c)
  }
}


