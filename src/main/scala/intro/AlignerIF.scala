// See README.md for license details.

package intro

import chisel3._

class AlignerIF(val data_width: Int) extends Bundle {
  val en   = Input  (Bool()) 
  val data = Input  (UInt(data_width.W)) 
}

