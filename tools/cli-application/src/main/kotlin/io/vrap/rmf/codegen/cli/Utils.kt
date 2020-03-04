package io.vrap.rmf.codegen.cli

/**
 * This method tries to run a block, if failed it will log the exception and not throw it
 */
fun safeRun(block: () -> Int): Int{
    return try{
        return block()
    } catch (e:Exception){
        e.printStackTrace()
        1
    }
}