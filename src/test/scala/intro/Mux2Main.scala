// See README.md for license details.

package intro

import chisel3._


object Mux2Main extends App {
  iotesters.Driver.execute(args, () => new Mux2) {
    c => new Mux2Tests(c)
  }
}


