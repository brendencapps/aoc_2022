package day16

import Puzzle
import PuzzleInput
import java.io.File
import kotlin.math.max

fun day16Puzzle() {
    Day16PuzzleSolution().solve(Day16PuzzleInput("inputs/day16/example.txt",1651))
    Day16PuzzleSolution().solve(Day16PuzzleInput("inputs/day16/input.txt", 1862))
    Day16Puzzle2Solution().solve(Day16PuzzleInput("inputs/day16/example.txt", 1707))
    Day16Puzzle2Solution().solve(Day16PuzzleInput("inputs/day16/input.txt", 2422))
}


class Day16PuzzleInput(val input: String, expectedResult: Int? = null) : PuzzleInput<Int>(expectedResult) {
    private val regex = "Valve (\\S*) has flow rate=(\\d*); tunnel(s?) lead(s?) to valve(s?) (.*)".toRegex()
    private val valveList =
        File(input).readLines().map { line ->
            val (valve, flow, _, _, _, connected) = regex.find(line)?.destructured ?: error("Error with $line")
            Valve(valve, flow.toInt(), connected.split(", "))
        }

    init {
        valveList.forEach{ valve -> valve.connectedValves.addAll(valveList.filter { v -> valve.connectedValvesInput.contains(v.name) })}
    }

    fun findMaxFlow(): Int {
        val initial = valveList.first { it.name == "AA"}
        val initialNode = Node(initial, 30, listOf(), valveList.filter { it.flowRate > 0 }, 0, 0)
        var currentNodes = listOf(initialNode)
        for(i in 1 .. 30) {
            currentNodes = currentNodes.flatMap { getNextNodes(it) }
            val maxFlow = currentNodes.maxOf{ it.flow * (it.minutesLeft-1) + it.totalFlow }
            currentNodes = currentNodes.filter { it.maxPotential() >= maxFlow }.distinctBy { it.valve.name + "_" + it.flow + "_" + it.totalFlow }
        }
        return currentNodes.maxOf { it.totalFlow }
    }

    fun findMaxFlow2(): Int {
        val initial = valveList.first { it.name == "AA"}
        val initialNode = Node2(initial, initial, 26, listOf(), valveList.filter { it.flowRate > 0 }, 0, 0)
        var currentNodes = listOf(initialNode)
        for(i in 1 .. 26) {
            currentNodes = currentNodes.flatMap { getNextNodes(it) }
            val maxFlow = currentNodes.maxOf{ it.flow * (it.minutesLeft-1) + it.totalFlow }
            currentNodes = currentNodes.filter { it.maxPotential() >= maxFlow }.distinctBy { it.myValve.name + "_" + it.elephantValve.name + "_" + it.flow + "_" + it.totalFlow }
        }
        return currentNodes.maxOf { it.totalFlow }
    }

    private fun getNextNodes(node: Node2): List<Node2> {
        val nextNodes = mutableListOf<Node2>()
        if(node.closedValves.isEmpty()) {
            return listOf(
                Node2(
                    node.myValve,
                    node.elephantValve,
                    node.minutesLeft - 1,
                    node.openValves,
                    node.closedValves,
                    node.flow,
                    node.totalFlow + node.flow)
            )
        }
        val canOpenMyValve = node.myValve.flowRate > 0 && node.closedValves.any { it == node.myValve}
        val canOpenElephantValve = node.elephantValve.flowRate > 0 && node.closedValves.any { it == node.elephantValve}
        if(canOpenMyValve || canOpenElephantValve) {
            if(node.myValve != node.elephantValve && canOpenMyValve && canOpenElephantValve) {
                nextNodes.add(
                    Node2(
                        node.myValve,
                        node.elephantValve,
                        node.minutesLeft - 1,
                        node.openValves + node.myValve + node.elephantValve,
                        node.closedValves.filter { it != node.myValve && it != node.elephantValve },
                        node.flow + node.myValve.flowRate + node.elephantValve.flowRate,
                        node.totalFlow + node.flow
                    )
                )
            }
            if(canOpenMyValve) {
                node.elephantValve.connectedValves.forEach { connectedValve ->
                    nextNodes.add(
                        Node2(
                            node.myValve,
                            connectedValve,
                            node.minutesLeft - 1,
                            node.openValves + node.myValve,
                            node.closedValves.filter { it != node.myValve },
                            node.flow + node.myValve.flowRate,
                            node.totalFlow + node.flow
                        )
                    )
                }
                if(node.closedValves.all { it.flowRate <= node.myValve.flowRate }) {
                    return nextNodes
                }
            }
            if(node.myValve != node.elephantValve && canOpenElephantValve) {
                node.myValve.connectedValves.forEach { connectedValve ->
                    nextNodes.add(
                        Node2(
                            node.elephantValve,
                            connectedValve,
                            node.minutesLeft - 1,
                            node.openValves + node.elephantValve,
                            node.closedValves.filter { it != node.elephantValve },
                            node.flow + node.elephantValve.flowRate,
                            node.totalFlow + node.flow
                        )
                    )
                }
                if(node.closedValves.all { it.flowRate <= node.elephantValve.flowRate }) {
                    return nextNodes
                }
            }
        }
        node.myValve.connectedValves.forEach { myConnectedValve ->
            node.elephantValve.connectedValves.forEach { elephantConnectedValve ->
                nextNodes.add(
                    Node2(
                        myConnectedValve,
                        elephantConnectedValve,
                        node.minutesLeft - 1,
                        node.openValves,
                        node.closedValves,
                        node.flow,
                        node.totalFlow + node.flow
                    )
                )
            }
        }
        return nextNodes
    }

    private fun getNextNodes(node: Node): List<Node> {
        val nextNodes = mutableListOf<Node>()
        if(node.closedValves.isEmpty()) {
            return listOf(
                Node(
                    node.valve,
                    node.minutesLeft - 1,
                    node.openValves,
                    node.closedValves,
                    node.flow,
                    node.totalFlow + node.flow)
            )
        }
        if(node.valve.flowRate > 0 && node.closedValves.any { it == node.valve}) {
            nextNodes.add(
                Node(
                    node.valve,
                    node.minutesLeft - 1,
                    node.openValves + node.valve,
                    node.closedValves.filter { it != node.valve},
                    node.flow + node.valve.flowRate,
                    node.totalFlow + node.flow))
            if(node.closedValves.all { it.flowRate <= node.valve.flowRate }) {
                return nextNodes
            }
        }
        node.valve.connectedValves.forEach { connectedValve ->
            nextNodes.add(
                Node(
                    connectedValve,
                    node.minutesLeft - 1,
                    node.openValves,
                    node.closedValves,
                    node.flow,
                    node.totalFlow + node.flow))
        }
        return nextNodes
    }
}


data class Valve(val name: String, val flowRate: Int, val connectedValvesInput: List<String>) {
    val connectedValves = mutableListOf<Valve>()
}

class Node(val valve: Valve, val minutesLeft: Int, val openValves: List<Valve>, val closedValves: List<Valve>, val flow: Int, val totalFlow: Int) {
    fun maxPotential(): Int {
        val remainingFlow = flow * (minutesLeft - 1) + totalFlow
        return remainingFlow + closedValves.mapIndexed { index, v -> v.flowRate * max(0, (minutesLeft - index * 2)) }.sum()
    }
}


class Node2(val myValve: Valve, val elephantValve: Valve, val minutesLeft: Int, val openValves: List<Valve>, val closedValves: List<Valve>, val flow: Int, val totalFlow: Int) {
    fun maxPotential(): Int {
        val remainingFlow = flow * (minutesLeft - 1) + totalFlow
        return remainingFlow + closedValves.mapIndexed { index, v -> v.flowRate * max(0, (minutesLeft - index * 2)) }.sum()
    }
}




class Day16PuzzleSolution : Puzzle<Int, Day16PuzzleInput>() {
    override fun solution(input: Day16PuzzleInput): Int {
        return input.findMaxFlow()
    }
}


class Day16Puzzle2Solution : Puzzle<Int, Day16PuzzleInput>() {
    override fun solution(input: Day16PuzzleInput): Int {
        return input.findMaxFlow2()
    }
}