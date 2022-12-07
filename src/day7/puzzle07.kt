package day7

import Puzzle
import PuzzleInput
import java.io.File

class FileSystemEntry(val name: String, val parent: FileSystemEntry? = null, private var size: Long = 0) {
    val children = mutableListOf<FileSystemEntry>()
    val isDir = size == 0.toLong()
    fun getEntrySize(): Long {
        if(size == 0.toLong() && children.isNotEmpty()) {
            size = children.sumOf { child ->
                child.getEntrySize()
            }
        }
        return size
    }
}

class Day7PuzzleInput(input: String, expectedResult: Long? = null) : PuzzleInput<Long>(expectedResult) {
    val root: FileSystemEntry = FileSystemEntry("/")

    init {
        val filePattern = "(\\d+) (.*)".toRegex()
        var currentDir = root
        File(input).readLines().forEach { line ->
            if(line.startsWith("$ cd ..")) {
                currentDir = currentDir.parent ?: currentDir
            }
            else if(line.startsWith("$ cd ")) {
                val dir = line.substring("$ cd ".length)
                currentDir = currentDir.children.find { it.name == dir } ?: currentDir
            }
            else if(line.startsWith("dir ")) {
                currentDir.children.add(FileSystemEntry(line.substring("dir ".length), currentDir))
            }
            else if(filePattern.matches(line)) {
                val (size, name) = filePattern.find(line)!!.destructured
                currentDir.children.add(FileSystemEntry(name, currentDir, size.toLong()))
            }
        }
        root.getEntrySize()
    }
}

class Day7PuzzleInputList(input: String, expectedResult: Long? = null) : PuzzleInput<Long>(expectedResult) {
    val root = FileSystemEntry("/")
    val dirQueue = mutableListOf<FileSystemEntry>()

    init {
        val filePattern = "(\\d+) (.*)".toRegex()
        var currentDir = root
        File(input).readLines().forEach { line ->
            if(line.startsWith("$ cd ..")) {
                currentDir = currentDir.parent ?: currentDir
            }
            else if(line.startsWith("$ cd ")) {
                val dir = line.substring("$ cd ".length)
                currentDir = currentDir.children.find { it.name == dir } ?: currentDir
            }
            else if(line.startsWith("dir ")) {
                val dir = FileSystemEntry(line.substring("dir ".length), currentDir)
                currentDir.children.add(dir)
                dirQueue.add(dir)
            }
            else if(filePattern.matches(line)) {
                val (size, name) = filePattern.find(line)!!.destructured
                currentDir.children.add(FileSystemEntry(name, currentDir, size.toLong()))
            }
        }
        root.getEntrySize()
    }
}

class Day7PuzzleSolution(private val targetDirSize: Long) : Puzzle<Long, Day7PuzzleInput>() {
    override fun solution(input: Day7PuzzleInput): Long {
        return getTotalSizeSmallDirectories(targetDirSize, input.root)
    }

    private fun getTotalSizeSmallDirectories(size: Long, entry: FileSystemEntry): Long {
        val sizeOfSmallChildren = entry.children.sumOf { child ->
            getTotalSizeSmallDirectories(size, child)
        }
        if(entry.isDir && entry.getEntrySize() <= size) {
            return sizeOfSmallChildren + entry.getEntrySize()
        }
        return sizeOfSmallChildren
    }
}

class Day7PuzzleSolutionList(private val targetDirSize: Long) : Puzzle<Long, Day7PuzzleInputList>() {
    override fun solution(input: Day7PuzzleInputList): Long {
        return input.dirQueue.sumOf {dir ->
            if(dir.getEntrySize() <= targetDirSize) {
                dir.getEntrySize()
            }
            else {
                0
            }
        }
    }

}

class Day7Puzzle2Solution : Puzzle<Long, Day7PuzzleInput>() {
    override fun solution(input: Day7PuzzleInput): Long {
        val targetDirSize = 30000000 - (70000000 - input.root.getEntrySize())
        val result = findDirectoryToDelete(targetDirSize, input.root)
        return result?.getEntrySize() ?: error("Did not find a directory")
    }

    private fun findDirectoryToDelete(size: Long, entry: FileSystemEntry): FileSystemEntry? {
        if(!entry.isDir || entry.getEntrySize() < size) return null

        var potentialDir: FileSystemEntry? = null
        for(child in entry.children) {
            val potentialChildDir = findDirectoryToDelete(size, child)
            if(potentialChildDir != null) {
                if(potentialDir == null || potentialDir.getEntrySize() > potentialChildDir.getEntrySize()) {
                    potentialDir = potentialChildDir
                }
            }
        }
        if(potentialDir != null) {
            return potentialDir
        }
        return entry
    }
}

class Day7Puzzle2SolutionList : Puzzle<Long, Day7PuzzleInputList>() {
    override fun solution(input: Day7PuzzleInputList): Long {
        val targetDirSize = 30000000 - (70000000 - input.root.getEntrySize())
        var currentBest = input.root
        for (dir in input.dirQueue) {
            if (dir.getEntrySize() < currentBest.getEntrySize() && dir.getEntrySize() > targetDirSize) {
                currentBest = dir
            }
        }
        return currentBest.getEntrySize()
    }
}

fun day7Puzzle() {
    Day7PuzzleSolution(100000).solve(Day7PuzzleInput("inputs/day7/exampleCommands.txt", 95437))
    Day7PuzzleSolution(100000).solve(Day7PuzzleInput("inputs/day7/inputCommands.txt",1428881))
    Day7PuzzleSolutionList(100000).solve(Day7PuzzleInputList("inputs/day7/exampleCommands.txt", 95437))
    Day7PuzzleSolutionList(100000).solve(Day7PuzzleInputList("inputs/day7/inputCommands.txt",1428881))
    Day7Puzzle2Solution().solve(Day7PuzzleInput("inputs/day7/exampleCommands.txt", 24933642))
    Day7Puzzle2Solution().solve(Day7PuzzleInput("inputs/day7/inputCommands.txt", 10475598))
    Day7Puzzle2SolutionList().solve(Day7PuzzleInputList("inputs/day7/exampleCommands.txt", 24933642))
    Day7Puzzle2SolutionList().solve(Day7PuzzleInputList("inputs/day7/inputCommands.txt", 10475598))
}



