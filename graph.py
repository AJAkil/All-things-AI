from vertex import Vertex

class Graph:
    def __init__(self):
        self.vertices = {} # A dictionary containing node name and object as value
        self.no_of_vertices = 0


    def add_vertex(self, vertex):
        if isinstance(vertex, Vertex) and vertex.name not in self.vertices:
            self.vertices[vertex.name] = vertex
            self.no_of_vertices += 1
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
                    #print(self.vertices[course])

    def greedy_color(self):

        max_colors = -999999

        # this is temporary, we get the list of all the keys of the graph
        vertices_list = list(self.vertices.keys())

        # a temporary dictionary to store the available colors
        # true for colors[index] means that the indexed vertex color is true
        # used for checking the colors of the neghbors of a certain vertex
        colors = [False for _ in vertices_list]

        # we assign first color to the first vertex
        self.vertices[vertices_list[0]].color = 0

        # now we assign colors to the reamining v-1 vertices
        for i in range(1, self.no_of_vertices):
            # we process all adjacent vertices of ith vertex and flag their colors as not available
            for neighbor in self.vertices[vertices_list[i]].neighbors:
                if self.vertices[neighbor.name].color != -1:
                    colors[self.vertices[neighbor.name].color] = True # marking the neighbor's color as True means unavailable.

            # find the first available color
            available_color = -1
            for index, state in enumerate(colors):
                if state == False:
                    available_color = index
                    break


            # assign the found color
            self.vertices[vertices_list[i]].color = available_color

            # keep track of the max color
            max_colors = max(max_colors, available_color)

            # resetting the value to false for next iteration
            for neighbor in self.vertices[vertices_list[i]].neighbors:
                if self.vertices[neighbor.name].color != -1:
                    colors[self.vertices[neighbor.name].color] = False

        print('Total time slots: ',max_colors+1)

    def print_allnode_colors(self):
        for index, node in self.vertices.items():
            print(f'node = {index} ---> color = {node.color}')



            

    


    
