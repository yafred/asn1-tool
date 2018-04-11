package com.yafred.asn1.runtime;

import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.BitSet;


public class AsnWriter {
    private PrintWriter writer;
    private ArrayList<Sequence> sequences = new ArrayList<Sequence>();
    private boolean isWaitingForChoiceValue = false;

    public AsnWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public void beginArray() {
        indentValue();
        sequences.add(new Sequence(sequences.size() + 1, true));
        writer.println("{");
    }

    public void endArray() {
        sequences.remove(sequences.size() - 1);
        indent();
        writer.println("}");
    }

    public void beginSequence() {
        indentValue();
        sequences.add(new Sequence(sequences.size() + 1, false));
        writer.println("{");
    }

    public void endSequence() {
        sequences.remove(sequences.size() - 1);
        indent();
        writer.println("}");
    }

    public void writeComponent(String componentName) {
        indentComponent();
        writer.print(componentName + " ");
    }

    public void writeSelection(String selectionName) {
        if ((sequences.size() != 0) &&
                ((Sequence) sequences.get(sequences.size() - 1)).isArray &&
                !isWaitingForChoiceValue) {
        	// we indent only in SEQUENCE OF and SET OF
            indentComponent();
        }

        writer.print(selectionName + " : ");
        isWaitingForChoiceValue = true;
    }

    public void writeInteger(java.lang.Integer value) {
        indentValue();
        writer.println(value);
    }

    public void writeBoolean(java.lang.Boolean value) {
        indentValue();

        boolean boolValue = value.booleanValue();

        if (boolValue) {
            writer.println("TRUE");
        } else {
            writer.println("FALSE");
        }
    }

    public void writeNull() {
        indentValue();
        writer.println("NULL");
    }

    public void writeEnumerated(String enumerated) {
        indentValue();
        writer.println(enumerated);
    }

    public void writeCharacterString(String value) {
        indentValue();
        writer.println("\"" + value + "\"");
    }

    public void writeIdentifier(String value) {
        indentValue();
        writer.println(value);
    }

    public void writeOctetString(byte[] value) {
        indentValue();
        writer.print("'" + bytesToString(value) + "'H");
        writer.print("  -- \"" + new String(value) + "\"");
        writer.println();
    }

    public void writeBitString(BitSet value) {
        int size = value.length();
        StringBuffer buffer = new StringBuffer();
        buffer.append("'");

        for (int i = 0; i < size; i++) {
            buffer.append(value.get(i) ? "1" : "0");
        }

        buffer.append("'B");
        writer.println(buffer.toString());
    }

    public void flush() {
        writer.flush();
    }

    private void indent() {
        if (sequences.size() != 0) {
            writer.print(((Sequence) sequences.get(sequences.size() - 1)).getIndent());
        }
    }

    private void indentComponent() {
        if (sequences.size() != 0) {
            Sequence sequence = (Sequence) sequences.get(sequences.size() - 1);

            if (sequence.isEmpty) {
                writer.print(sequence.getIndent());
            } else {
                writer.print(",");
                writer.print(sequence.getIndent().substring(1));
            }

            sequence.setEmpty(false);
        }
    }

    private void indentValue() {
        if (sequences.size() != 0) {
            Sequence sequence = (Sequence) sequences.get(sequences.size() - 1);

            if (sequence.isArray() && !isWaitingForChoiceValue) {
                if (sequence.isEmpty) {
                    writer.print(sequence.getIndent());
                } else {
                    writer.print(",");
                    writer.print(sequence.getIndent().substring(1));
                }

                sequence.setEmpty(false);
            }
        }

        isWaitingForChoiceValue = false;
    }

    private static String bytesToString(byte[] buffer) {
        String text = "";

        for (int i = 0; i < buffer.length; i++) {
            String byteText = Integer.toHexString((int) buffer[i]);

            switch (byteText.length()) {
            case 1:
                byteText = "0" + byteText;

                break;

            case 2:
                break;

            default:
                byteText = byteText.substring(byteText.length() - 2,
                        byteText.length());

                break;
            }

            if (i == 0) {
                text = byteText;
            } else {
                text += (" " + byteText);
            }
        }

        return text;
    }

    private class Sequence {
        private boolean isArray = false;
        private boolean isEmpty = true;
        private String indent = "";

        public Sequence(int rank, boolean isArray) {
            for (int i = 0; i < rank; i++) {
                indent += "  ";
            }

            this.isArray = isArray;
        }

        public String getIndent() {
            return indent;
        }

        public void setEmpty(boolean isEmpty) {
            this.isEmpty = isEmpty;
        }

        public boolean isArray() {
            return isArray;
        }
    }
}
