package testcases

import (
	"generated-code/g_003"
	"testing"
)

func Test_g003_1(t *testing.T) {
	var value g_003.MyNull
	testWritePdu(&value, []byte{0x42, 0x00}, t)

	testReadPdu(&value, []byte{0x42, 0x00}, t)
}
