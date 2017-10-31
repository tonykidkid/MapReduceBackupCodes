#!/bin/env python
# encoding=utf-8

import sys

if __name__ == "__main__":
    avg_usage_rate = sys.argv[1]
    min_rate = sys.argv[2]
    avg_ = float(avg_usage_rate)
    min_ = float(min_rate)
    # write()必须写到一个字符串中，否则报错TypeError: expected a string or other character buffer object
    sys.stdout.write(str(avg_ - min_))  # 结果将由shell脚本接收
