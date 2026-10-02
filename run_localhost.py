#!/usr/bin/env python3
"""
MasjidKU TV - Localhost Web Remote Server Runner
Menjalankan Web Remote Server di http://localhost:8080
"""

import os
import sys

base_dir = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, os.path.join(base_dir, "web_remote"))

from server import run_server

if __name__ == "__main__":
    run_server()
