from graph import *
import os
import sys

iterations = [100000]
choices = ['degree_sorted', 'dsatur']

dirs = os.listdir('.\\final_tests')
f2 = open('log2.txt','a+')

f2.write('  '.join(['file name', 'iterations', 'slots', 'penalty', 'strategy','\n\n']))

for file in dirs:
    f = open(os.path.join('.\\final_tests',file),'r+')

    for choice in choices:
        for iters in iterations:
            g = Graph()
            g.construct_graph(f)
            g.greedy_color(choice=choice) if choice != 'dsatur' else g.dsatur_algo_eff()
            g.stochastic_hill_climbing(int(iters),f)
            f2.write('  '.join([f.name.split('\\')[2], str(iters) , str(g.colors_needed), str(g.minimum_penalty), choice+' + kempe chain ', '\n']))

        f2.write(f'*********************{choice} scheme ends here*************************************\n')

    f2.write('-------------------------------------------------------------------\n\n')
    f2.write('\n\n')

