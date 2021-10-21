package testcases

import (
	"bytes"
	"testing"

	"github.com/yafred/asn1-go/ber"
)

type berAble interface {
	WritePdu(writer *ber.Writer) error
	ReadPdu(reader *ber.Reader) error
}

func testWritePdu(pdu berAble, data []byte, t *testing.T) {
	writer := ber.NewWriter(-1)

	error := pdu.WritePdu(writer)

	if error != nil {
		t.Fatal("Wrong", error)
	}

	if bytes.Equal(writer.GetDataBuffer(), data) == false {
		t.Fatal("Wrong", writer.GetDataBuffer(), "expected", data)
	}
}

func testReadPdu(pdu berAble, data []byte, t *testing.T) {
	in := bytes.NewReader(data)
	reader := ber.NewReader(in)

	error := pdu.ReadPdu(reader)

	if error != nil {
		t.Fatal("Wrong", error)
	}
}
