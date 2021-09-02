package testcases

import (
	"testing"

	"generator/output/go/dummy"
)

func TestHello(t *testing.T) {
	value := dummy.Hello()

	if value != "hello" {
		t.Fatal("Wrong")
	}
}
