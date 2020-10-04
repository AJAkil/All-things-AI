# import random as rd
# # f = open('yor-f-83.stu','r+')
# # print(f.name)
# # import sys

# # print(sys.argv[1])


# # i = 0
# # for row in f:
# #     courses = row.split()
# #     courses = [int(c) for c in courses]
# #     l = len(courses)-1

# #     for index, course in enumerate(courses):
# #         print(course)
# #         if index != l:
# #             print(courses[index+1:])

# # class A:
# #     def __init__(self, n):
# #         self.name = n
# #         self.x = []

# # a = A('akil')
# # b = A('kokil')

# # a.x.append(b)
# # print(a.x[0].name)
# # b.name='monir'
# # print(a.x[0].name)

# a=10
# b=20

# a = max(a,b)
# # print(a)

# # a = Vertex('A')
# # b = Vertex('B')
# # c = Vertex('C')
# # d = Vertex('D')
# # e = Vertex('E')

# # a.add_neighbors_undirected([b,c,e]) 
# # b.add_neighbors_undirected([a,c])
# # c.add_neighbors_undirected([b,d,a,e])
# # d.add_neighbor_undirected(c)
        
        
# # g = Graph()
# # g.add_vertices([a,b,c,d,e])
# # g.add_edge_undirected(b,d)
# # ass = Vertex('ass')
# # pasa = Vertex('Pasa')
# # g.add_vertices([ass, pasa])
# # g.add_edge_undirected(ass, pasa)
# # g.add_edge_undirected(ass,d)
# # g.add_edge_undirected(c, pasa)
# # g.print_graph()
# # g = Graph()
# # a = Vertex(1)
# # b = Vertex(2)
# # c = Vertex(3)
# # d = Vertex(4)
# # e = Vertex(5)
# # f = Vertex(6)
# # g.add_vertices([a,b,c,d,e,f])
# # g.add_edge_undirected(a,b)
# # g.add_edge_undirected(a,b)
# # g.add_edge_undirected(a,b)
# # g.add_edge_undirected(c,d)
# # g.add_edge_undirected(c,d)
# # g.add_edge_undirected(c,d)
# # g.print_graph()
# # g.add_edge_undirected(b,e)
# # g.add_edge_undirected(c,e)
# # g.add_edge_undirected(e,d)
# # g.print_graph()
# # g.greedy_color()
# # g.print_allnode_colors()

# # print(max(list(g.get_all_vertices_degree().values())))
# #g.print_graph()
# #g.greedy_color(choice='degree_sorted')
# #g.print_all_neighbor_color()

# class testNode:
#     def __init__(self, v):
#         self.v = v

#     def __repr__(self):
#         return str(self.v)

# a = testNode(1)
# b = testNode(2)
# c = testNode(3)

# v = [a, b, c]

# t = v[rd.randint(0,len(v)-1)]

# print(t)

# v.pop(v.index(t))

# print(v)
from graph import *


file_name = input('Enter the name of the file please: ')

f = open(file_name,'r+')
g = Graph()
g.construct_graph(f)

#g.print_graph()

g.dsatur_algo_naive()
g.cal_avg_penalty(f)
g.print_result(f)

