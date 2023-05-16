from generate_points import generate_points
from construct_hull import construct_hull
from display_hull import display_hull

if __name__ == "__main__":
    num_points = 500

    # Generate random points
    points = generate_points(num_points)

    # Construct convex hull
    hull = construct_hull(points)

    # Display convex hull
    display_hull(hull)