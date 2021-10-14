package testcases

import "github.com/yafred/asn1-go/ber"

type berAble interface {
	WritePdu(writer *ber.Writer) error
	ReadPdu(reader *ber.Reader) error
}

func readPdu(pdu *berAble) {
}
