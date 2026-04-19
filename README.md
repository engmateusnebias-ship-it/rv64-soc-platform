# rv64-soc-platform

[![CI](https://github.com/engmateusnebias-ship-it/rv64-soc-platform/actions/workflows/ci.yml/badge.svg)](https://github.com/engmateusnebias-ship-it/rv64-soc-platform/actions/workflows/ci.yml)

A parametric RV64GC SoC platform capable of booting Linux, with professional
verification via UVM and riscv-formal, and an industrial U-Mode application
for power grid frequency monitoring (50 Hz Europe).

## Application

The SoC runs a real-time power grid frequency monitor in U-Mode. It samples
frequency pulses via GPIO, computes frequency in Hz, and reports via UART:

    [OK]    50.0 Hz
    [ALERT] 47.3 Hz — undervoltage event

A deviation counter tracks events over time. In Verilator simulation, the
frequency signal is injected via a test model — no physical hardware required.

## Architecture

RV64GC SoC
┌─────────────────────────────────────────────────────┐
│                                                     │
│   Rocket Core (RV64GC)          L1 I$ / D$ 32KB    │
│   M / S / U privilege modes     4-way set assoc.   │
│                                                     │
│              TileLink (TL-UL / TL-UH / TL-C)        │
│   ┌──────────────────────────────────────────┐      │
│   │           Crossbar (XBAR)                │      │
│   └───┬──────────┬──────────┬───────┬────────┘      │
│       │          │          │       │               │
│     DRAM       CLINT       PLIC   UART/SPI/GPIO     │
│   0x8000_0000 0x0200_0000 0x0C00_0000 0x1000_0000  │
└─────────────────────────────────────────────────────┘

## Stack

| Layer               | Technology                          |
|---------------------|-------------------------------------|
| RTL generator       | Rocket Chip (chipsalliance/master)  |
| HDL                 | Chisel 6.7.0 + firtool 1.144.0      |
| Build tool          | Mill 0.11.7                         |
| Language            | Scala 2.13                          |
| JDK                 | OpenJDK 21                          |
| Simulation          | Verilator 5.x                       |
| Integration tests   | Cocotb (Python)                     |
| Protocol verif.     | UVM (SystemVerilog IEEE 1800.2)     |
| ISA verification    | riscv-formal + SAIL golden model    |
| Firmware            | OpenSBI (M-Mode)                    |
| OS                  | Linux kernel (RISC-V)               |
| Userspace           | BusyBox + C frequency monitor       |

## Memory map

| Base address   | Size   | Peripheral     |
|----------------|--------|----------------|
| `0x0000_0000`  | 64 KB  | Boot ROM       |
| `0x0200_0000`  | 64 KB  | CLINT          |
| `0x0C00_0000`  |  4 MB  | PLIC           |
| `0x1000_0000`  |  4 KB  | UART 16550     |
| `0x1000_1000`  |  4 KB  | SPI            |
| `0x1000_2000`  |  4 KB  | GPIO (16x)     |
| `0x8000_0000`  |  1 GB  | DRAM           |

## Boot sequence
ZSBL (Boot ROM)
└── OpenSBI (M-Mode firmware)
└── Linux kernel (S-Mode)
└── BusyBox init (U-Mode)
└── freq-monitor (U-Mode application)

## Build

```bash
# Requirements: JDK 21, Mill 0.11.7, firtool 1.144.0, Verilator 5.x
# RISC-V toolchains: riscv64-unknown-elf-gcc, riscv64-linux-gnu-gcc

# Clone with submodules
git clone --recursive https://github.com/engmateusnebias-ship-it/rv64-soc-platform.git

# Verify SoC configuration
mill soc.runMain config.RV64SoCTop

# Run Cocotb integration tests
make -C verif/cocotb

# Run UVM regression
make -C verif/uvm
```

## Status

| Phase | Description                        | Status         |
|-------|------------------------------------|----------------|
| 1     | Environment setup                  | ✅ Complete    |
| 2     | Rocket Chip + Mill + CI            | ✅ Complete    |
| 3     | Verilator + Cocotb smoke tests     | 🔄 In progress |
| 4     | Peripherals + Device Tree          | ⬜ Planned     |
| 5     | OpenSBI boot                       | ⬜ Planned     |
| 6     | Linux + BusyBox boot               | ⬜ Planned     |
| 7     | UVM TileLink verification          | ⬜ Planned     |
| 8     | riscv-formal ISA checking          | ⬜ Planned     |

## License

MIT
