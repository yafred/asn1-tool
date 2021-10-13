package testcases

import "github.com/yafred/asn1-go/ber"

type berAble interface {
	writePdu(writer *ber.Writer)
	readPdu(reader *ber.Reader)
}

func readPdu(pdu *berAble) {
}
