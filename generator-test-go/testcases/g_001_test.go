package testcases

import (
	"bytes"
	"generated-code/g_001"
	"testing"

	"github.com/yafred/asn1-go/ber"
)

func Test_g001_1(t *testing.T) {
	var value g_001.MyInteger = 0

	in := bytes.NewReader([]byte{0x02, 0x01, 0x0a})

	reader := ber.NewReader(in)

	error := value.ReadPdu(reader)

	if error != nil {
		t.Fatal("Wrong", error)
	}

	if value != 10 {
		t.Fatal("value should be 10")
	}

}

func Test_g001_2(t *testing.T) {
	var value g_001.ColorType

	value.SetNavyBlue()

	if !value.IsNavyBlue() {
		t.Fatal("Wrong:", value)
	}
}
