package testcases

import (
	"generated-code/g_005"
	"testing"
)

func Test_g005_1(t *testing.T) {
	var value g_005.Type1 = "Hi!"
	testWritePdu(&value, []byte{0x1a, 0x03, 0x48, 0x69, 0x21}, t)

	value = ""
	testReadPdu(&value, []byte{0x1a, 0x03, 0x48, 0x69, 0x21}, t)
	if value != "Hi!" {
		t.Fatal("Wrong", value, "expected", "Hi!")
	}
}
