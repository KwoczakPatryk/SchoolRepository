#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Jun 26 17:39:19 2019

@author: patrykkwoczak
"""

import numpy as np
from matplotlib import pyplot as plt
import imageio


delta_vals = np.linspace(0,2*np.pi,10)
count = 1
images = []
#for th in delta_vals:
    
radius = 1 #unit circle
arg = 2*np.pi/3#7*np.pi/8
w = [0,np.exp(1j*arg),np.exp(1j*-arg),1] #in form (z-w_0)(z-w_1)...(z-w_n-1)
p1 = np.poly(w)
#p1 = np.poly1d([1,0,0,-1/3,0])
dp1 = np.polyder(p1)

def p_circle(x,r):
    return np.sqrt(r**2 - x**2)

def n_circle(x,r):
    return -1*np.sqrt(r**2 - x**2)
x = np.linspace(-1,1,500)
y_p = p_circle(x,radius)
y_n = n_circle(x,radius)

roots = np.roots(p1)
criticals = np.roots(dp1)
rootsRe, rootsImag, criticalRe, criticalImag = [],[],[],[]
for k in range(len(roots)):
    rootsRe.append(roots[k].real)
    rootsImag.append(roots[k].imag)
    if k < len(roots)-1:
        criticalRe.append(criticals[k].real)
        criticalImag.append(criticals[k].imag)
plt.figure(count)    
plt.plot(x,y_p,c = 'black')
plt.plot(x,y_n,c = 'black')
plt.gca().set_aspect('equal', adjustable='box')
plt.scatter(rootsRe,rootsImag, marker = 'o',edgecolor = 'blue')
plt.scatter(criticalRe,criticalImag, marker = 'x', c = 'red')
title = "theta: " #+ str(th)
plt.title(title)
plt.show()
    
#    fig_name = "unit_disk_roots_fig_" + str(count)
#    fig_name = str('/Users/patrykkwoczak/pythonLearning/unit_disk_roots_figures'+'/'+fig_name + '.png')
#    plt.savefig(fig_name)
#    images.append(imageio.imread(fig_name))
#    
#    count += 1
#    
#imageio.mimsave('/Users/patrykkwoczak/pythonLearning/animRoots.gif',images)