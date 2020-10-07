from graph import *
import os
import sys

num_of_exp = input('Enter the number of experiments: ')
iterations = [ int(input('number of iteration: ' )) for _ in range(int(num_of_exp))]

choices = ['random', 'degree_sorted', 'dsatur']

dirs = os.listdir('.\\final_tests')
f2 = open('log4.txt','a+')

f2.write('  '.join(['file name', 'iterations', 'slots', 'penalty', 'strategy','\n\n']))

for file in dirs:
    f = open(os.path.join('.\\final_tests',file),'r+')

    for choice in choices:
        for iters in iterations:
            g = Graph()
            g.construct_graph(f)
            g.greedy_color(choice=choice) if choice != 'dsatur' else g.dsatur_algo_eff()
            g.stochastic_hill_climbing(int(iters),f)

            sol_name = f.name.split('\\')[2].split('.')[0]
            sol = open(f'{sol_name}-{choice}-kempe.sol','a+')
            g.write_to_file(sol)

            f2.write('  '.join([f.name.split('\\')[2], str(iters) , str(g.colors_needed), str(g.minimum_penalty), choice+' + kempe chain ', '\n']))

        f2.write(f'*********************{choice} scheme ends here*************************************\n\n')

    f2.write('----------------------------------------------------------------------------------------\n\n')
    f2.write('\n\n')

