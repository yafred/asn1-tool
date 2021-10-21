package testcases

import (
	"generated-code/g_001"
	"testing"
)

func Test_g001_1(t *testing.T) {
	var value g_001.MyInteger = 10
	testWritePdu(&value, []byte{0x02, 0x01, 0x0a}, t)

	value = 0
	testReadPdu(&value, []byte{0x02, 0x01, 0x0a}, t)
	if value != 10 {
		t.Fatal("Wrong", value, "expected 10")
	}
}

func Test_g001_2(t *testing.T) {
	var value g_001.ColorType
	value.SetNavyBlue()

	if !value.IsNavyBlue() {
		t.Fatal("Wrong:", value)
	}
	testWritePdu(&value, []byte{0x02, 0x01, 0x02}, t)

	value = 0
	testReadPdu(&value, []byte{0x02, 0x01, 0x01}, t)
	if value.IsWhite() != true {
		t.Fatal("Wrong", value, "expected 1")
	}
}
