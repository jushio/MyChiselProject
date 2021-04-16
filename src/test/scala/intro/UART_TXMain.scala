// See README.md for license details.

package intro

import chisel3._


object UART_TXMain extends App {
  iotesters.Driver.execute(args, () => new UART_TX(1, 19200)) {
    c => new UART_TXTests(c)
  }
}


