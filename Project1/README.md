# Airline Profit Maximization Project
CMPE160 - Introduction to Object Oriented Programming 
Bogazici University 2022 Spring CMPE160 project

Instructor: Tuna Tuğcu
TA: Yiğit Yıldırım
SA: Bahadır Gezer bahadir.gezer@boun.edu.tr

This project was designed to make students capable of handling big projects with the basic principles of OOP, prepared by the hardwork of Bahadır Gezer and Yiğit Yıldırım.
This repo contains the codes I have written as solutions to the project task. Some descriptive files about the project problem is also included, which are prepared by Bahadır Gezer.

## Overview Of the Project 
There were two tasks in the project:
1) Implementing multiple classes to simulate an Airline Company
2) Developing Heuristics to maximize the profit of this Airline

Students were given a pdf file explainig the architecture of classes, their attributes and methods. Description file of the project can be found [here](https://github.com/ArdaSaygan/CMPE160/files/9739265/p1_description.pdf)

Airlines, aircrafts, airports and passengers were represented by their own interfaces. Each of them were implemented by their concrete classes. For example  Passenger abstract class were implemented by EconomyPassenger, LuxuryPassenger, FirstClassPassenger classes. There was a variety of different types for aircrafts, airports and passengers. These are listed below.

### Types of Aircraft:
There are 4 types of aircraft each with its own use case. 

- **Widebody:** Long range, high capacity widebody passenger aircraft
- **Rapid:** Medium to long range, medium capacity fast passenger aircraft
- **Jet:** Short range, low capacity luxury passenger jet
- **Prop:** Short range, medium capacity turboprop aircraft

### Types of Passenger:
There are 4 types of passenger each with different properties and needs.

- **Economy Passenger**
- **Business Passenger**
- **First Class Passenger**
- **Luxury Passenger**

### Types of Airport:
Different types of airports serve different purposes.

- **Hub Airport**
- **Major Airport**
- **Regional Airport**

Every class had various functionalities. For example aircraft classes have methods to refuel, fly; passenger classes have methods to load, unload from an aircraft. These are all listed in the project description file.

After implementing these classes I came up with an algortihm to gain profit. It was implemented in the main class.
