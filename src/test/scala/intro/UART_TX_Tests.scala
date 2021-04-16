// See README.md for license details.

package intro

import scala.math.ceil

import chisel3.iotesters.PeekPokeTester


class UART_TXTests(c: UART_TX) extends PeekPokeTester(c) {
  //CLK_RATE = 19200, type = 1(9600bps)
  val R = 19200.0
  val T = 9600.0
  val count = ceil(R/T) toInt
  val data_in = 0xaa

  // Check initial output
  expect(c.io.BUSY, 0)
  expect(c.io.DI.ready, 1)
  expect(c.io.TX, 1)

  poke(c.io.DI.valid, 0)
  step(1)

  // Check output after clk
  expect(c.io.BUSY, 0)
  expect(c.io.DI.ready, 1)
  expect(c.io.TX, 1)

  poke(c.io.DI.valid, 1)
  poke(c.io.DI.bits, data_in)
  step(1)

  // Check output after valid
  println("Check output after valid")
  expect(c.io.BUSY, 1)
  expect(c.io.DI.ready, 0)
  expect(c.io.TX, 0)

  for (b <- 0 until 10) {
    for (s <- 0 until count) {
      if (s == count - 1) {
        println("Check output before TX change")
        expect(c.io.BUSY, 1)
        expect(c.io.DI.ready, 0)
        expect(c.io.TX, b%2)
      }
      step(1)
    }
    printf("loop%d Check output\n", b)
    // Check output after valid
    expect(c.io.BUSY, 1)
    expect(c.io.DI.ready, 0)
    expect(c.io.TX, (b+1)%2)
  }

}


