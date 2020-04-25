// See README.md for license details.

package intro

import chisel3._


class Hello extends Module {
  val io = IO(new Bundle{})
  printf("hello world\n")
}

object Hello extends App {
  chisel3.Driver.execute(args, () => new Hello)
}


