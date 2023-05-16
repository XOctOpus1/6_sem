import random
from typing import List
from point import Point

def generate_points(num_points: int) -> List[Point]:
    points = []
    for i in range(num_points):
        x = random.randint(0, 100)
        y = random.randint(0, 100)
        points.append(Point(x, y))
    return points