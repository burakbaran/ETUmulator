/*
 * Copyright (C) 2017 Kasirgalabs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kasirgalabs.etumulator.processor;

import static org.junit.Assert.assertEquals;
import com.kasirgalabs.etumulator.JavaFXThread;
import com.kasirgalabs.etumulator.linker.Linker;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

public class GUISafeProcessorTest {
    @Rule
    public final JavaFXThread javaFXThread = new JavaFXThread();
    private final ProcessorUnits processorUnits;
    private final GUISafeProcessor processor;
    private final Linker linker;

    public GUISafeProcessorTest() {
        processorUnits = new ProcessorUnits();
        processor = new GUISafeProcessor(processorUnits);
        linker = new Linker(processorUnits.getMemory());
    }

    @After
    public void tearDown() {
        processor.stop();
    }

    /**
     * Test of run method, of class GUISafeProcessor.
     *
     * @throws java.lang.InterruptedException
     * @throws java.util.concurrent.ExecutionException
     * @throws java.util.concurrent.TimeoutException
     */
    @Test
    public void testRun() throws InterruptedException, ExecutionException, TimeoutException {
        String code = "nop\n"
                + "nop\n"
                + "nop\n";
        processor.run(code, linker.link(code));
        processor.waitForComplete(1, TimeUnit.SECONDS);

        code = "mov r0, #1\n"
                + "label:\n"
                + "cmp r0, #100\n"
                + "beq exit\n"
                + "add r0, r0, #1\n"
                + "bl uart_write\n"
                + "b label\n"
                + "exit:\n";
        processor.run(code, linker.link(code));
        processor.waitForComplete(2, TimeUnit.SECONDS);
        assertEquals("GUISafeProcessor does not work properly.", 100, processorUnits
                .getRegisterFile().getValue("r0"));

        code = "mov r0, #1\n"
                + "label:\n"
                + "cmp r0, #100\n"
                + "beq exit\n"
                + "add r0, r0, #1\n"
                + "push {r0}\n"
                + "b label\n"
                + "exit:\n";
        processor.run(code, linker.link(code));
        processor.waitForComplete(2, TimeUnit.SECONDS);
        assertEquals("GUISafeProcessor does not work properly.", 100, processorUnits
                .getStack().peek());
    }
}