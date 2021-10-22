package testcases

import (
	"generated-code/g_002"
	"testing"
)

func Test_g002_1(t *testing.T) {
	var value g_002.MyBoolean = true
	testWritePdu(&value, []byte{0x62, 0x03, 0x01, 0x01, 0xff}, t)

	value = false
	testReadPdu(&value, []byte{0x62, 0x03, 0x01, 0x01, 0xff}, t)
	if value != true {
		t.Fatal("Wrong", value, "expected true")
	}
}
