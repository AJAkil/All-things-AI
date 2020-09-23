from vertex import Vertex

class Graph:
    def __init__(self):
        self.vertices = {} # A dictionary containing node name and object as value


    def add_vertex(self, vertex):
        if isinstance(vertex, Vertex) and vertex.name not in self.vertices:
            self.vertices[vertex.name] = vertex
            return True
        else:
            return False

    def add_vertices(self, vertices):
        for vertex in vertices:
            self.add_vertex(vertex)

    def add_edge_undirected(self, vertex_from, vertex_to):
        if isinstance(vertex_from, Vertex) and isinstance(vertex_to, Vertex):
            if vertex_from.name in self.vertices and vertex_to.name in self.vertices:
                self.vertices[vertex_from.name].add_neighbor_undirected(vertex_to)
                return True
            else:
                return False
        else:
            return False

    def add_edge_directed(self, vertex_from, vertex_to):
        if isinstance(vertex_from, Vertex) and isinstance(vertex_to, Vertex):
            if vertex_from.name in self.vertices and vertex_to.name in self.vertices:
                self.vertices[vertex_from.name].add_neighbor_directed(vertex_to)
                return True
            else:
                return False
        else:
            return False

    def print_graph(self):
        for key in self.vertices.keys():
            print(str(self.vertices[key]))
            print()

    def get_all_vertices_degree(self):
        return {key: value.get_vertex_degree() for key, value in self.vertices.item()}

    def construct_graph(self, file_object):
        for row in file_object:
            courses = row.split()
            
            # we form the vertex object if they are not already made
            for index, c in enumerate(courses):
                if c not in self.vertices:
                    # if it is not in our graph, we create a vertex object
                    course_vertex = Vertex(c)
                    self.add_vertex(course_vertex)

            for i, course in enumerate(courses):
                for j, neighbor in enumerate(courses[i+1:]):
                    self.vertices[course].add_neighbor_undirected(self.vertices[neighbor])

    


    
