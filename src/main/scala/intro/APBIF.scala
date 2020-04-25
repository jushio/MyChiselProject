// See README.md for license details.

package intro

import chisel3._

class APBIF(val addr_width: Int, val data_width: Int) extends Bundle {
  val PADDR   = Input  (UInt(addr_width.W))
  val PPROT   = Input  (UInt(2.W))
  val PSEL    = Input  (Bool())
  val PENABLE = Input  (Bool()) 
  val PWRITE  = Input  (Bool()) 
  val PWDATA  = Input  (UInt(data_width.W)) 
  val PSTRB   = Input  (UInt((data_width/8).W)) 
  val PREADY  = Output (Bool()) 
  val PRDATA  = Output (UInt(data_width.W)) 
  val PSLVERR = Output (Bool()) 
}

