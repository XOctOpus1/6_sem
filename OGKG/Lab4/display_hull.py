# display_hull.py

import matplotlib.pyplot as plt
from typing import List
from hull import Hull
from point import Point

def display_hull(hull: Hull) -> None:
    x_coords = [point.x for point in hull]
    y_coords = [point.y for point in hull]
    plt.plot(x_coords, y_coords)
    plt.show()
