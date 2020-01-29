#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Jun 21 20:28:47 2019

@author: patrykkwoczak
"""
import numpy as np
from matplotlib import pyplot as plt
import cmath as c
from scipy.integrate import odeint
import random as rand

alpha = 3 - np.sqrt(8)

def Nflow(z,t):
    dzdt = -1 * np.polyval(p1,z) / np.polyval(dp1,z) #*2* ((abs(np.polyval(dp1,z)))**2)
    return dzdt

def Nmap(z,p,dp,step):
    return z - step * ( np.polyval(p, z) / np.polyval(dp, z))

def line(x0,y0,slope,x):
    return slope*(x-x0)+y0

coeff = [1,0,-1,0,1] #change as you wish
p1 = np.poly1d(coeff)

#arg = 7*np.pi/8
#w = [0,1,np.exp(1j*arg),np.exp(1j*-arg)] #in form (z-w_0)(z-w_1)...(z-w_n-1)
#p1 = np.poly(w)


roots = np.roots(p1)#Find Roots
dp1 = np.polyder(p1)
criticals = np.roots(dp1)#Find Critical Points
P_crits = np.polyval(p1, criticals) #Mapping c points to P_target

#####       Variables       #####

ang_change = 0 #to see results, change by about .05
bound = 2 #edges of the map
chosen_root = len(P_crits) #0: midray of basins 1 degree, len(degree): midrays of inner basin
step = 1 #stepsize of Newton's method
plot_target = False

##################################
P_crit_args = []
for k in P_crits:
    P_crit_args.append(c.phase(k)) #find angles of P_crits

for k in range(len(P_crit_args)):
    while P_crit_args[k] > 2*np.pi or P_crit_args[k] < 0: #put between 0 < arg < 2pi
        if P_crit_args[k] > 2*np.pi:
            P_crit_args[k] = 2*np.pi - P_crit_args[k]
        if P_crit_args[k] < 0:
            P_crit_args[k] = 2*np.pi + P_crit_args[k]

d2p1 = np.polyder(dp1)
inflections = np.roots(d2p1)#Frind inflection Points
P_inf = np.polyval(p1,inflections)
    
difference = [] #take difference of 2 adjacent angles, half it, and put inbetween them
for k in range(len(P_crits)):
    if k < len(P_crits)-1: 
        difference.append(P_crit_args[k+1] - P_crit_args[k])
        
        
    else:
        difference.append(((P_crit_args[0]) - ((P_crit_args[k]))))
    
    dist = difference[k]
    flag_d = 0
    if dist > 2*np.pi or dist < 0: #find any angles that give the wrong side of the point
        flag_d = 1
    
    if flag_d == 0:
        difference[k] = difference[k]/2 
    else:
        difference[k] = difference[k]/2  + np.pi
mid_ray = difference
for k in range(len(difference)):
    mid_ray[k] = difference[k] + P_crit_args[k]
    
ray_points = []
for k in range(len(mid_ray)):
    ray_points.append(abs(1)*np.exp(1j*mid_ray[k])) #P_crits[k]
    

source_rayPoints = []
for k in ray_points:
    source_rayPoints.append(np.roots(p1-k)[chosen_root-1])#[len(P_crits)])


xmin, ymin = -bound,-bound
xmax,ymax = bound,bound
   
zero_arr = np.zeros(len(P_crits))
P_critsRe,P_critsImag,RpRe,RpImag, P_infRe, P_infImag = [],[],[],[],[],[]
for k in range(len(P_crits)):
    P_critsRe.append(P_crits[k].real)
    P_critsImag.append(P_crits[k].imag)
    RpRe.append(ray_points[k].real)
    RpImag.append(ray_points[k].imag)
    if k < len(P_crits)-1:
        P_infRe.append(P_inf[k].real)
        P_infImag.append(P_inf[k].imag)

if plot_target == True:  
    plt.figure(1) #image in C_target
    plt.scatter(zero_arr,zero_arr)
    plt.scatter(P_critsRe,P_critsImag, marker = 'x', color = "green")
    plt.scatter(RpRe,RpImag, marker = 'd', color = "purple")
    plt.scatter(P_infRe,P_infImag, marker = '+', color = "blue")
    plt.grid(True)
    plt.xlim(xmin,xmax)
    plt.ylim(ymin,ymax)
    #plt.gca().set_aspect('equal', adjustable='box')
    title_str = "C_Target: a: ",coeff[1],", b: ",coeff[2]
    plt.title(title_str)
    plt.draw()


#make points on plot for roots, critical points, and ray_points
reRoots, reCrits, reSRP, reInf = [], [], [], []
imagRoots,imagCrits , imagSRP, imagInf = [], [], [], [], 
length = len(roots) 
for k in range(len(roots)):
    reRoots.append(roots[k].real)
    imagRoots.append(roots[k].imag)
for k in range(len(criticals)):
    reCrits.append(criticals[k].real)
    imagCrits.append(criticals[k].imag)
    reSRP.append(source_rayPoints[k].real)
    imagSRP.append(source_rayPoints[k].imag)
for k in inflections:
    reInf.append(k.real)
    imagInf.append(k.imag)
plt.figure(2) #image in C_source
plt.scatter(reRoots,imagRoots, color = "r")
plt.scatter(reCrits,imagCrits, marker = 'x', color = "black", s = 60)
#plt.scatter(reSRP, imagSRP, marker = 'd', s = 80, color = 'purple')
plt.scatter(reInf, imagInf, marker = '+',color = "blue")
plt.grid(True)
plt.xlim(xmin,xmax)
plt.ylim(ymin,ymax)
#plt.gca().set_aspect('equal', adjustable='box')
title_source = "C_Source: change in ang = ",ang_change, " Step Size = " ,step
title_source = title_source, "a: ",coeff[1],", b: ",coeff[2]
plt.title("Newton Flow")
plt.draw()



zreal = np.linspace(xmin,xmax,25)
zimag = np.linspace(ymin,ymax,25)
Zreal, Zimag = np.meshgrid(zreal, zimag)
C_source = Zreal + 1j *Zimag

t = 0
u,v = np.zeros(Zreal.shape), np.zeros(Zimag.shape)

NK, NC = Zreal.shape

for k in range(NK):
    for ii in range(NC):
        ZPrime = Nflow(C_source[ii,k],t)
        ZPrime = ZPrime/abs(ZPrime) #NORMALIZE CURVES
        u[ii,k] = ZPrime.real
        v[ii,k] = ZPrime.imag

Q = plt.quiver(Zreal, Zimag, u,v,color = "black")

#Create solution curves
x_ray, y_ray = [],[]
for k in range(len(source_rayPoints)):
    x_ray.append(reSRP[k])
    y_ray.append(imagSRP[k])

seedpoints = np.array([x_ray,y_ray])
#plt.streamplot(Zreal, Zimag, u, v, linewidth = 2, start_points = seedpoints.T, density = 30)

#plot sepimatrices
c_real, c_imag = [],[]
for k in range(len(criticals)):
    c_real.append(reCrits[k])
    c_imag.append(imagCrits[k])
#PLOT 
    
#choose distance from origin
r = bound
start_N = []
path_real, path_imag = [],[]
for k in range(len(source_rayPoints)): #initial points
    current_ang = source_rayPoints[k]
    start_N.append(r*np.exp(1j*(c.phase(current_ang + ang_change))))
    
iteration_val = 30
for k in range(len(start_N)):
        path_real.append(start_N[k].real)
        path_imag.append(start_N[k].imag)
        
        val_current = Nmap(start_N[k],p1,dp1,step)
        for ii in range((iteration_val)):
            path_real.append(val_current.real)
            path_imag.append(val_current.imag)
            
            val_current = Nmap(val_current,p1,dp1,step)
            
        

#plt.scatter(path_real,path_imag,facecolor = 'none', edgecolor = 'r')
for k in criticals:
    print(c.phase(k))