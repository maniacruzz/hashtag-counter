#!/usr/bin/python/
##########################
#Hashtagcounter          #
#Created by: Amogh Rao   #
#University of Florida   #
##########################
import operator
f =  open('sampleInput_Million.txt')
lines=f.readlines()
dic={}
for line in lines:
	if(line.lower()=='stop'):
		break
	if(line[0]=='#'):
		x = line[1:-1].split(" ")
		if x[0] not in dic.keys():
			dic[x[0]]=int(x[1])	
		else:
			dic[x[0]]+=int(x[1])
	else:
		print('')
		out=""
		n=sorted(dic.values())
		sorted_x = sorted(dic.items(), key=operator.itemgetter(1))[::-1]
		for i in range(int(line)):
			out+=(sorted_x[i][0]+",")
		print(out[:-1]),
		#temp={}
		#for i in range(int(line)):
		#	for key in dic:
		#		if dic[key]==n[i]:
		#			temp[key]=n[i]
		#			dic[key]=0
		#	n[i] 
		#	print(int(line))
print('')
#print(dic)
