package config

import chisel3._

/**
 * RV64GC SoC Platform — top-level configuration parameters.
 *
 * This object defines the memory map and hardware parameters for the
 * rv64-soc-platform. Parameters will be consumed by peripheral modules,
 * the interconnect, and the software stack (device tree, OpenSBI platform).
 *
 * Rocket Chip generator integration is added in Phase 3.
 */
object SoCParameters {

  // -----------------------------------------------------------------------
  // Core
  // -----------------------------------------------------------------------
  val XLEN         = 64      // RV64
  val NUM_CORES    = 1
  val RESET_VECTOR = BigInt("00001000", 16)  // Boot ROM base address

  // -----------------------------------------------------------------------
  // Memory map — must match dts/rv64soc.dts and sw/opensbi platform
  // -----------------------------------------------------------------------
  val BOOT_ROM_BASE = BigInt("00000000", 16)
  val BOOT_ROM_SIZE = BigInt("00010000", 16)  // 64 KB

  val CLINT_BASE    = BigInt("02000000", 16)
  val CLINT_SIZE    = BigInt("00010000", 16)  // 64 KB

  val PLIC_BASE     = BigInt("0C000000", 16)
  val PLIC_SIZE     = BigInt("00400000", 16)  // 4 MB

  val UART_BASE     = BigInt("10000000", 16)
  val UART_SIZE     = BigInt("00001000", 16)  // 4 KB

  val SPI_BASE      = BigInt("10001000", 16)
  val SPI_SIZE      = BigInt("00001000", 16)  // 4 KB

  val GPIO_BASE     = BigInt("10002000", 16)
  val GPIO_SIZE     = BigInt("00001000", 16)  // 4 KB
  val GPIO_WIDTH    = 16

  val DRAM_BASE     = BigInt("80000000", 16)
  val DRAM_SIZE     = BigInt("40000000", 16)  // 1 GB

  // -----------------------------------------------------------------------
  // UART
  // -----------------------------------------------------------------------
  val UART_CLOCK_FREQ = 50000000  // 50 MHz
  val UART_BAUD_RATE  = 115200

  // -----------------------------------------------------------------------
  // PLIC
  // -----------------------------------------------------------------------
  val PLIC_NUM_SOURCES  = 32
  val PLIC_NUM_TARGETS  = 2   // M-mode + S-mode

  // -----------------------------------------------------------------------
  // Caches (L1)
  // -----------------------------------------------------------------------
  val ICACHE_SIZE_KB = 32
  val DCACHE_SIZE_KB = 32
  val CACHE_WAYS     = 4
}

/**
 * Top-level elaboration entry point.
 * Prints the SoC memory map to stdout for verification.
 * RTL generation will be added when Rocket Chip is integrated (Phase 3).
 */
object RV64SoCTop extends App {
  import SoCParameters._

  println("=" * 60)
  println("rv64-soc-platform — memory map")
  println("=" * 60)
  println(f"Boot ROM   0x${BOOT_ROM_BASE}%08X  ${BOOT_ROM_SIZE / 1024}%d KB")
  println(f"CLINT      0x${CLINT_BASE}%08X  ${CLINT_SIZE / 1024}%d KB")
  println(f"PLIC       0x${PLIC_BASE}%08X  ${PLIC_SIZE / 1024 / 1024}%d MB")
  println(f"UART       0x${UART_BASE}%08X  ${UART_SIZE / 1024}%d KB")
  println(f"SPI        0x${SPI_BASE}%08X  ${SPI_SIZE / 1024}%d KB")
  println(f"GPIO       0x${GPIO_BASE}%08X  ${GPIO_SIZE / 1024}%d KB  (${GPIO_WIDTH} pins)")
  println(f"DRAM       0x${DRAM_BASE}%08X  ${DRAM_SIZE / 1024 / 1024}%d MB")
  println("=" * 60)
  println(f"Core: RV${XLEN}GC  Cores: ${NUM_CORES}  Reset vector: 0x${RESET_VECTOR}%08X")
  println("=" * 60)
}
