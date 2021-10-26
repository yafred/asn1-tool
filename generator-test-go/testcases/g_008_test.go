package testcases

import (
	"generated-code/g_008"
	"testing"
)

func Test_g008_2(t *testing.T) {
	var value g_008.Occupation

	value.SetClerk(true)
	testWritePdu(&value, []byte{0x03, 0x02, 0x07, 0x80}, t)

	value = g_008.Occupation{}
	testReadPdu(&value, []byte{0x03, 0x02, 0x07, 0x80}, t)
	if value.GetClerk() != true {
		t.Fatal("Wrong", "expected clerk")
	}
}
