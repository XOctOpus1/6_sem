# hull.py

from typing import List
from point import Point

class Hull:
    def __init__(self):
        self.points = []

    def add_point(self, point: Point) -> None:
        self.points.append(point)

    def __len__(self):
        return len(self.points)

    def __getitem__(self, index):
        return self.points[index]

    def __str__(self):
        return str(self.points)
