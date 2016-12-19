import pandas as pd
import numpy as np

a = pd.read_csv("results_hier.txt",sep="\t")

df = a[28:]
df.columns = ["idx","it","loss"]
