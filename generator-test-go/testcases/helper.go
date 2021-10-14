package testcases

import "github.com/yafred/asn1-go/ber"

type berAble interface {
	WritePdu(writer *ber.Writer)
	ReadPdu(reader *ber.Reader)
}

func readPdu(pdu *berAble) {
}
