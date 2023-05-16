# construct_hull.py

from typing import List
from hull import Hull
from point import Point
from left_turn import left_turn

def construct_hull(points: List[Point]) -> Hull:
    # Sort points by x-coordinate
    sorted_points = sorted(points, key=lambda p: p.x)

    # Initialize an empty stack
    hull_stack = []

    # Add first two points to stack
    hull_stack.append(sorted_points[0])
    hull_stack.append(sorted_points[1])

    # Add remaining points to stack
    for i in range(2, len(sorted_points)):
        while len(hull_stack) >= 2 and not left_turn(hull_stack[-2], hull_stack[-1], sorted_points[i]):
            hull_stack.pop()
        hull_stack.append(sorted_points[i])

    # Add remaining points to hull
    hull = Hull()
    for point in hull_stack:
        hull.add_point(point)

    return hull
