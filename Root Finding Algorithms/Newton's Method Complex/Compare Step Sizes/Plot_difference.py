#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Tue Jun  4 22:55:37 2019

@author: patrykkwoczak
"""

import numpy as np
from matplotlib import pyplot as plt
from matplotlib import colors as c
from scipy.misc import derivative
import random as rand

#This will plot the basins when called
def get_polybasins(f, roots, bound, xmin, xmax, ymin, ymax, numpoints = 300, 
                iters = 35, colormap = 'brg', step_size = 1): 
    
    #create real and imaginary axis and join them together to make points of Z_old
    zreal = np.linspace(xmin, xmax, numpoints)
    zimag = np.linspace(ymin, ymax, numpoints)
    Zreal, Zimag = np.meshgrid(zreal, zimag)
    Z_old = Zreal + 1j *Zimag
    P_old = Zreal + 1j *Zimag
    
    #create roots that will be graphed
    roots_real, roots_imag = [np.zeros(len(roots)),np.zeros(len(roots))]
    for k in range(len(roots)):
        roots_real[k] = roots[k].real
        roots_imag[k] = roots[k].imag
    
    #iterate through Newton's Method
    dp1 = np.polyder(p1)
    for k in range(iters):
        p_i = np.polyval(p1,Z_old)
        dp_i = np.polyval(dp1,Z_old)
        
        Z_new = Z_old - step_size*(((p_i)) / (dp_i)) #Newton's Method
        Z_old = Z_new
        
    #take input from what the true root value is, and see which they are closest to
    Z_Graph = np.zeros((numpoints,numpoints)) #generate the graph's coords
    for k in range(numpoints):
        for c in range(numpoints):
            #record which root it's closest to
            for ii in range(len(roots)):
                current_root = roots[ii]
                current_entry = Z_new[c,k]
                difference = current_root - current_entry
                if  abs(difference) < 10e-6: #if they are almost at the same place
                    Z_Graph[c,k] = ii+1 #assign color value
            #record any periodic points
    
    
    
    for k in range(numpoints):
        for c in range(numpoints):
            P_old[c,k] = Z_Graph[c,k]
            
            
    #iterate to find critical values
    criticals = np.roots(dp1)
    criticals_real, criticals_imag = [np.zeros(len(criticals)), np.zeros(len(criticals))]
    for k in range(len(criticals)):
        criticals_real[k] = criticals[k].real
        criticals_imag[k] = criticals[k].imag
    
                   
#    title = "Step Size: "
#    title2 = str(step_size)
#    title = title,title2
#    #Note: roots won't need to be given to plot_basins if using another method to find such as
#    #path lifting.
#    plt.pcolormesh(Zreal, Zimag, Z_Graph, cmap = colormap)
#    plt.scatter(roots_real, roots_imag,c = 'white', label = 'Roots')
#    plt.scatter(criticals_real, criticals_imag,marker = "x",c = 'black', label = 'Critical')
#    #plt.scatter(zreal, zimag,)
#    plt.title(title)
#    plt.show()
    
    return Z_Graph

def plot_diff(graph1, graph2, xmin, xmax, ymin, ymax, numpoints, step1, step2):
    graph_diff = graph1 - graph2
    zreal = np.linspace(xmin, xmax, numpoints)
    zimag = np.linspace(ymin, ymax, numpoints)
    Zreal, Zimag = np.meshgrid(zreal, zimag)
    
    #Note: roots won't need to be given to plot_basins if using another method to find such as
    #path lifting.
    
    for k in range(numpoints):
        for c in range(numpoints):
            if graph_diff[c,k] != 0:
                graph_diff[c,k] = 1
                
                
    title = ['Step of ']
    title.append(step1)
    title.append(" minus step of ")
    title.append(step2)
    plt.title(title)
    plt.pcolormesh(Zreal, Zimag, graph_diff, cmap = 'binary')
    plt.show()

def find_bound(coeff_vals): #cauchy bounds
    coeff_Cauchy = []
    length = len(coeff_vals)
    for k in range(length):
        coeff_Cauchy.append(abs(coeff_vals[k] / coeff_vals[0]))

    return 1 + max(coeff_Cauchy)

    

#create coefficients
#coeff = np.zeros(20)
#for k in range(len(coeff)):
#    coeff[k] = rand.randint(-100000,100000)
    
coeff = [1,0,0,-1,0]  
#coeff = [2,-3,7,1]   
p1 = np.poly1d(coeff)

#arg = 7*np.pi/8
#w = [0,1,np.exp(1j*arg),np.exp(1j*-arg)]
#p1 = np.poly(w)

roots = np.roots(p1)
bound = 2# find_bound(coeff) #another func needed for when roots are not given

iteration_num = 50
resolution = 800
step1 = 1
xmin, ymin = [-bound, -bound] #shortcut to assign multiple values
xmax, ymax = [bound, bound]

#Note: if stepsize is less than .2, then the iteration_num must increase 
g1 = get_polybasins(p1, roots, bound, xmin, xmax, ymin, ymax, resolution, iteration_num, 'brg',step1)
step2 = .2
iteration_num = 300
g2 = get_polybasins(p1, roots, bound, xmin, xmax, ymin, ymax, resolution, iteration_num, 'brg',step2)
plot_diff(g1, g2, xmin, xmax, ymin, ymax, resolution, step1, step2)