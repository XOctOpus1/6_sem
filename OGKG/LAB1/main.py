import numpy as np
import matplotlib.pyplot as plt


"""
    Функція для зчитування координат вершин з файлу vertices.txt

    :param filename: ім'я файлу vertices.txt
    :return: масив координат вершин у форматі (x, y)
"""
def read_vertices(filename):
    with open(filename, 'r', encoding='utf-8') as f:
        vertices = []
        for line in f:
            x, y = line.strip().split()
            vertices.append(np.array([float(x), float(y)]))
        return vertices



"""
    Функція для зчитування ребер з файлу edges.txt

    :param filename: ім'я файлу edges.txt
    :return: список ребер у форматі (індекс початкової вершини, індекс кінцевої вершини)
"""
def read_edges(filename):
    with open(filename, 'r', encoding='utf-8') as f:
        edges = []
        for line in f:
            v1, v2 = line.strip().split()
            edges.append((int(v1), int(v2)))
        return edges




"""
    Функція для визначення, з якої сторони вектора (p1 - p0) знаходиться точка p2

    :param p0: координати початкової точки вектора у форматі (x, y)
    :param p1: координати кінцевої точки вектора у форматі (x, y)
    :param p2: координати точки, яку потрібно перевірити у форматі (x, y)
    :return: True, якщо точка p2 знаходиться ліворуч від вектора (p1 - p0), і False - якщо справа
"""
def is_left(p1, p2, p):
    return np.cross(p2 - p1, p - p1) > 0




"""
    Функція для локалізації точки на планарному розбитті методом трапецій

    :param point: координати точки у форматі (x, y)
    :param vertices: масив координат вершин у форматі (x, y)
    :param edges: список ребер у форматі (індекс початкової вершини, індекс кінцевої вершини)
    :return: індекс грані, на якій знаходиться точка

"""
def localize_point(point, vertices, edges):
    # шукаємо трикутник, в якому знаходиться точка
    for i, (v1, v2, v3) in enumerate(zip(vertices[:-2], vertices[1:-1], vertices[2:])):
        if is_left(v1, v2, point) and is_left(v2, v3, point) and is_left(v3, v1, point):
            return i

    # шукаємо грань, на якій знаходиться точка
    for i, (v1, v2) in enumerate(edges):
        if is_left(vertices[v1], vertices[v2], point):
            return i + len(vertices) - 2

    # якщо точка знаходиться за межами планарного розбиття, повертаємо None
    return None




def plot_graph(vertices, edges):
    fig = plt.figure()
    ax = fig.add_subplot(111)

    for v in vertices:
        ax.plot(v[0], v[1], 'ro')

    for e in edges:
        ax.plot([vertices[e[0]][0], vertices[e[1]][0]],
                [vertices[e[0]][1], vertices[e[1]][1]], 'b-')

    plt.show()




# приклад використання функцій для локалізації точки
vertices = read_vertices(r'C:\Users\dd111\OneDrive\Documents\unic\3term\6_sem\OGKG\LAB1\vertices.txt')
edges = read_edges(r'C:\Users\dd111\OneDrive\Documents\unic\3term\6_sem\OGKG\LAB1\edges.txt')
point = np.array([10, 5])  # приклад координат точки
print(localize_point(point, vertices, edges))
plot_graph(vertices, edges)





