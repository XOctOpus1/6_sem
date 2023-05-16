from point import Point

def left_turn(p1: Point, p2: Point, p3: Point) -> bool:
    return (p2.x - p1.x) * (p3.y - p1.y) - (p3.x - p1.x) * (p2.y - p1.y) > 0
