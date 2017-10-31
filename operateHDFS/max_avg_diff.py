#!/bin/env python
# encoding=utf-8

import sys

if __name__ == "__main__":
    max_rate = sys.argv[1]
    avg_usage_rate = sys.argv[2]
    max_ = float(max_rate)
    avg_ = float(avg_usage_rate)
    # write()必须写到一个字符串中，否则报错TypeError: expected a string or other character buffer object
    sys.stdout.write(str(max_ - avg_))  # 结果将由shell脚本接收
